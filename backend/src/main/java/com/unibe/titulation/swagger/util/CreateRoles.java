package com.unibe.titulation.swagger.util;

/**
 * EJECUTAR UNA SOLA VEZ


@Component
public class CreateRoles implements CommandLineRunner {

    @Autowired
    RolService rolService;

    @Override

    public void run(String... args) throws Exception {
         Rol rolAdmin = new Rol(RolName.ROLE_ADMIN);
        Rol rolUser = new Rol(RolName.ROLE_USER);
         Rol rolStudent = new Rol(RolName.ROLE_STUDENT);
         Rol rolSecretary = new Rol(RolName.ROLE_SECRETARY);
         Rol rolCareerDirector= new Rol(RolName.ROLE_CAREER_DIRECTOR);
         Rol rolLiableTT= new Rol(RolName.ROLE_LIABLE_TT);
         Rol rolReader= new Rol(RolName.ROLE_READER);
         Rol rolDean= new Rol(RolName.ROLE_DEAN);
         Rol rolAcademicDirector= new Rol(RolName.ROLE_ACADEMIC_DIRECTOR);
         Rol rolTeacher= new Rol(RolName.ROLE_TEACHER);
         Rol rolFinancial= new Rol(RolName.ROLE_FINANCIAL);
        rolService.save(rolAdmin);
        rolService.save(rolUser);
         rolService.save(rolStudent);
         rolService.save(rolSecretary);
         rolService.save(rolCareerDirector);
         rolService.save(rolLiableTT);
         rolService.save(rolReader);
         rolService.save(rolDean);
         rolService.save(rolAcademicDirector);
         rolService.save(rolTeacher);
        rolService.save(rolFinancial);
    }
} */
