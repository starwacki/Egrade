package com.github.starwacki.components.teacher;
import com.github.starwacki.components.teacher.dto.TeacherResponseDTO;
import com.github.starwacki.components.teacher.dto.TeacherSchoolClassDTO;
import com.github.starwacki.components.teacher.exceptions.TeacherNotFoundException;
import com.github.starwacki.components.teacher.exceptions.TeacherSchoolClassException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeacherFacade {

    private final TeacherRepository teacherRepository;


    /**
     * Get teacher classes return all classes which the teacher teaches
     * @param teacherId  - id taken by logged teacher account
     * @return List of teacher schoolClassDTO
     */
    public List<TeacherSchoolClassDTO> getTeacherClasses(int teacherId) {
        return teacherRepository.findTeacherById(teacherId)
                .map(teacher -> getTeacherSchoolClassInDTOList(teacher))
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));

    }

    public void addSchoolClassToTeacher(int teacherId, TeacherSchoolClassDTO teacherSchoolClassDTO ) {
        Teacher accountTeacher = getTeacherById(teacherId);
        addSchoolClassToTeacher(accountTeacher,teacherSchoolClassDTO);
    }

    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherRepository
                .findAll()
                .stream()
                .map(teacher -> TeacherMapper.mapTeacherToTeacherResponseDTO(teacher))
                .collect(Collectors.toList());
    }

    private void addSchoolClassToTeacher(Teacher teacher,TeacherSchoolClassDTO teacherSchoolClassDTO) {
        TeacherSchoolClass teacherSchoolClass = TeacherMapper.mapTeacherSchoolClassDTOToTeacherSchoolClass(teacherSchoolClassDTO);
        if (!isTeacherHaveSchoolClass(teacher,teacherSchoolClass))
            saveTeacherWithNewSchoolClass(teacher,teacherSchoolClass);
        else
            throw new TeacherSchoolClassException(teacherSchoolClassDTO);
    }

    private void saveTeacherWithNewSchoolClass(Teacher teacher, TeacherSchoolClass teacherSchoolClass) {
        Set<TeacherSchoolClass> teacherSchoolClassSet = new HashSet<>(teacher.getTeacherSchoolClass());
        teacherSchoolClassSet.add(teacherSchoolClass);
        teacher.setTeacherSchoolClass(teacherSchoolClassSet);
        teacherRepository.save(teacher);
    }

    private boolean isTeacherHaveSchoolClass(Teacher teacher, TeacherSchoolClass teacherSchoolClass) {
        return teacher.getTeacherSchoolClass().contains(teacherSchoolClass);
    }


    private List<TeacherSchoolClassDTO> getTeacherSchoolClassInDTOList(Teacher teacher) {
        return teacher.getTeacherSchoolClass()
                .stream()
                .map(teacherSchoolClass -> TeacherMapper.mapTeacherSchoolClassToTeacherSchoolClassDTO(teacherSchoolClass))
                .toList();
    }


    private Teacher getTeacherById(int id) {
        return teacherRepository
                .findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
    }

}
