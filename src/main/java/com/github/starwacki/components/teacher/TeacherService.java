package com.github.starwacki.components.teacher.service;

import com.github.starwacki.components.teacher.dto.TeacherDTO;
import com.github.starwacki.components.teacher.dto.SchoolClassDTO;
import com.github.starwacki.components.teacher.exceptions.SchoolClassNotFoundException;
import com.github.starwacki.components.teacher.exceptions.TeacherNotFoundException;
import com.github.starwacki.components.teacher.mapper.SchoolClassMapper;
import com.github.starwacki.components.teacher.mapper.TeacherMapper;
import com.github.starwacki.components.account.AccountTeacher;
import com.github.starwacki.common.model.school_class.SchoolClass;
import com.github.starwacki.common.repositories.SchoolClassRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final SchoolClassRepository schoolClassRepository;


    /**
     * Get teacher classes return all classes which the teacher teaches
     * @param teacherId  - id taken by logged teacher account
     * @return List of teacher schoolClassDTO
     */
    public List<SchoolClassDTO> getTeacherClasses(int teacherId) {
        return teacherRepository.findTeacherById(teacherId)
                .getClasses()
                .stream()
                .map(schoolClass -> SchoolClassMapper.mapSchoolClassToSchoolClassDTO(schoolClass))
                .collect(Collectors.toList());
    }

    public void addSchoolClassToTeacher(int teacherId, SchoolClassDTO schoolClassDTO ) {
        AccountTeacher accountTeacher = getTeacherById(teacherId);
        SchoolClass schoolClass = getSchoolClassByNameAndYear(schoolClassDTO);
        accountTeacher.setClasses(getSchoolClassSetWithAddedClass(accountTeacher,schoolClass));
        teacherRepository.save(accountTeacher);
    }

    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository
                .findAll()
                .stream()
                .map(teacher -> TeacherMapper.mapTeacherToTeacherDTO(teacher))
                .collect(Collectors.toList());
    }


    private AccountTeacher getTeacherById(int id) {
        return teacherRepository
                .findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
    }

    private Set<SchoolClass> getSchoolClassSetWithAddedClass(AccountTeacher accountTeacher, SchoolClass schoolClass) {
        Set<SchoolClass> set = new HashSet<>(accountTeacher.getClasses());
        set.add(schoolClass);
        return set;
    }

    private SchoolClass getSchoolClassByNameAndYear(SchoolClassDTO schoolClassDTO) {
        return schoolClassRepository
                .findSchoolClassByNameAndAndClassYear(schoolClassDTO.className(),schoolClassDTO.year())
                .orElseThrow(() -> new SchoolClassNotFoundException(schoolClassDTO.className(), schoolClassDTO.year()));
    }

}
