package com.github.starwacki.components.student;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentService {

//    private final StudentRepository studentRepository;
//    private final SchoolClassRepository schoolClassRepository;
//
//    public List<StudentDTO> getAllStudentsFromClass(String className, int classYear) {
//        return studentRepository
//                .findAllBySchoolClassNameAndSchoolClassClassYear(className,classYear)
//                .stream()
//                .map(student -> StudentMapper.mapStudentToStudentDTO(student))
//                .toList();
//    }
//
//    public void changeStudentClass(int id, String className, int year) {
//       studentRepository
//                .findById(id)
//                .map(student -> setSchoolClass(student,className,year))
//                .orElseThrow(() -> new StudentNotFoundException(id));
//    }
//
//    private AccountStudent setSchoolClass(AccountStudent accountStudent, String className, int year) {
//        accountStudent.setSchoolClass(getSchoolClass(className,year));
//        studentRepository.save(accountStudent);
//        return accountStudent;
//    }
//
//    private SchoolClass getSchoolClass(String className, int year) {
//        return schoolClassRepository
//                .findSchoolClassByNameAndAndClassYear(className,year)
//                .orElse(createNewSchoolClass(className,year));
//    }
//
//    private SchoolClass createNewSchoolClass(String className, int year) {
//        return new SchoolClass(className,year);
//    }


}
