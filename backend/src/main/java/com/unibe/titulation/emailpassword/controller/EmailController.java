package com.unibe.titulation.emailpassword.controller;

import com.unibe.titulation.dtos.Message;
import com.unibe.titulation.emailpassword.dto.ChangePasswordDTO;
import com.unibe.titulation.emailpassword.dto.EmailValuesDTO;
import com.unibe.titulation.emailpassword.service.EmailService;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.service.UserService;
import com.unibe.titulation.services.TopicStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.internet.AddressException;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/email")
@CrossOrigin
public class EmailController {

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TopicStudentService topicStudentService;

    @PostMapping("/sendResetPasswordEmail")
    public ResponseEntity<Message> sendResetPasswordEmail(@RequestBody EmailValuesDTO dto) {
        Optional<User> userOptional = userService.getByUserNameOrEmail(dto.getMailTo());
        if (!userOptional.isPresent())
            return new ResponseEntity<>(new Message("Te hemos enviado un correo"), HttpStatus.OK);
        User user = userOptional.get();
        dto.setMailTo(user.getEmail());
        UUID uuid = UUID.randomUUID();
        String tokenPassword = uuid.toString();
        dto.setTokenPassword(tokenPassword);
        user.setTokenPassword(tokenPassword);
        userService.save(user);
        emailService.sendResetPasswordEmail(dto);
        return new ResponseEntity<>(new Message("Te hemos enviado un correo"), HttpStatus.OK);
    }

    @PostMapping("/sendToReader/{studentId}/{readerEmail}")
    public ResponseEntity<Message> sendDocument(@RequestBody MultipartFile file,
                                                @PathVariable String studentId, @PathVariable String readerEmail) {

        emailService.sendThesisDocumentToReader(file, readerEmail);
        topicStudentService.updateThesisSentStatus(studentId);
        return new ResponseEntity<>(new Message("Su documento ha sido enviado al lector"), HttpStatus.OK);
    }

    @PostMapping("/sendToStudent/{topicId}")
    public ResponseEntity<Message> sendAntiPlagiarismLetterToStudent(@PathVariable String topicId,
                                                                     @RequestBody MultipartFile file) throws AddressException {
        emailService.sendAntiPlagiarismLetterToStudent(file, topicId);
        topicStudentService.updateAntiPlagiarismLetterSentStatus(topicId);
        return new ResponseEntity<>(new Message("Se ha enviado el correo con el documento adjunto"), HttpStatus.OK);

    }

    @GetMapping("/checkTokenPassword/{tokenPassword}")
    public boolean checkIfUserHasTokenPassword(@PathVariable String tokenPassword) {
        Optional<User> user = this.userService.getByTokenPassword(tokenPassword);
        if (user.isPresent())
            return true;
        return false;
    }

    @PostMapping("/change-password")
    public ResponseEntity<Message> changePassword(@Valid @RequestBody ChangePasswordDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Message("Los campos están incompletos"), HttpStatus.BAD_REQUEST);
        if (!dto.getPassword().equals(dto.getConfirmPassword()))
            return new ResponseEntity(new Message("Las contraseñas no coinciden"), HttpStatus.BAD_REQUEST);
        Optional<User> usuarioOpt = userService.getByTokenPassword(dto.getTokenPassword());
        if (!usuarioOpt.isPresent())
            return new ResponseEntity(new Message("No existe ningún usuario con esas credenciales"), HttpStatus.NOT_FOUND);
        User user = usuarioOpt.get();
        String newPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(newPassword);
        user.setTokenPassword(null);
        userService.save(user);
        return new ResponseEntity(new Message("Contraseña actualizada"), HttpStatus.OK);
    }

}
