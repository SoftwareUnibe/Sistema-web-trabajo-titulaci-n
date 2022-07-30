package com.unibe.titulation.security.controller;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.security.dto.LoginUser;
import com.unibe.titulation.security.dto.NewUser;
import com.unibe.titulation.security.dto.UpdateUser;
import com.unibe.titulation.security.entity.Rol;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.enums.RolName;
import com.unibe.titulation.security.jwt.JwtProvider;
import com.unibe.titulation.security.service.RolService;
import com.unibe.titulation.security.service.UserService;
import com.unibe.titulation.security.util.CookieUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Value("${jwt.accesTokenCookieName}")
    private String accessTokenCookieName;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private UserService userService;

    private RolService rolService;

    private JwtProvider jwtProvider;

    @Autowired
    public AuthController(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
            UserService userService, RolService rolService, JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.rolService = rolService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(HttpServletResponse httpServletResponse, @Valid @RequestBody LoginUser loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
            if (this.userService.getByCi(loginRequest.getUserName()).isPresent() && !this.userService.getByCi(loginRequest.getUserName()).get().isVerified())
                return new ResponseEntity<>(new Message("Su cuenta aún no ha sido verificada"), HttpStatus.BAD_REQUEST);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = this.jwtProvider.generateToken(authentication);
            CookieUtil.create(httpServletResponse, accessTokenCookieName, jwt, true, -1, "localhost");
            return new ResponseEntity<>(new Message(""), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<Message> logOut(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.clear(response, accessTokenCookieName);
        return new ResponseEntity<>(new Message("Sesión cerrada"), HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);
        if (userService.existByCi(newUser.getCi()))
            return new ResponseEntity<>(new Message("Cédula no válida"), HttpStatus.BAD_REQUEST);
        if (userService.existsByEmail(newUser.getEmail()))
            return new ResponseEntity<>(new Message("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);
        User user = new User(newUser.getName(), newUser.getCi(), newUser.getEmail(), newUser.getLastName(),
                newUser.getSecondName(), newUser.getSecondLastName(), newUser.getCi(),
                passwordEncoder.encode(newUser.getPassword()),
                false, newUser.getDegree());
        Set<Rol> roles = new HashSet<>();
        if (newUser.getCareer() != null) {
            user.setCareer(newUser.getCareer());
        }
        Object[] rolesArray = newUser.getRoles().toArray();
        for (int i = 0; i < newUser.getRoles().size(); i++) {
            RolName rm = RolName.valueOf(rolesArray[i].toString());
            roles.add(rolService.getByRolName(rm).get());
        }
        if (newUser.getRoles().contains("admin"))
            roles.add(rolService.getByRolName(RolName.ROLE_ADMIN).get());
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(new Message("usuario guardado"), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UpdateUser updateUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("campos mal puestos"), HttpStatus.BAD_REQUEST);
        Optional<User> userData = userService.getByCi(updateUser.getCi());
        if (!userData.isPresent()) {
            return new ResponseEntity<>(new Message("Usuario no encotrado"), HttpStatus.BAD_REQUEST);
        }
        User user = userData.get();
        user.setEmail(updateUser.getEmail());
        user.setName(updateUser.getName());
        user.setSecondName(updateUser.getSecondName());
        user.setLastName(updateUser.getLastName());
        user.setSecondLastName(updateUser.getSecondLastName());
        userService.save(user);
        return new ResponseEntity<>(new Message("Usuario actualizado"), HttpStatus.CREATED);
    }

    @GetMapping("/details")
    public Optional<User> getUserByCi() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String ci = userDetails.getUsername();
        return this.userService.getByCi(ci);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DEAN')")
    @GetMapping("/admin/allUsers")
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DEAN')")
    @PatchMapping("/admin/verifiedUser/{userCi}")
    public ResponseEntity<Message> userVerification(@PathVariable String userCi) {
        Optional<User> user = this.userService.getByCi(userCi);
        if (!user.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe"), HttpStatus.BAD_REQUEST);
        }
        User verifiedUser = user.get();
        verifiedUser.setVerified(true);
        this.userService.save(verifiedUser);
        return new ResponseEntity<>(new Message("Usuario verificado correctamente"), HttpStatus.OK);
    }
}
