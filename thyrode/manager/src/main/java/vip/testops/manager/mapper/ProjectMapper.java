package vip.testops.manager.mapper;

import org.apache.ibatis.annotations.*;
import vip.testops.manager.entity.dto.ProjectDTO;
import vip.testops.manager.entity.vto.ProjectVTO;

import java.util.List;

@Mapper
public interface ProjectMapper {

    @Select("select * from t_project")
    List<ProjectDTO> getProjectList();

    @Insert("insert into t_project values(null,#{projectName},#{description},#{status},now(),now())")
    int addProject(ProjectDTO projectDTO);

    @Update("update t_project set projectName=#{projectName},decription=#{decprition}" +
            "where projectId=#{projectId}")
    int modifyProject(ProjectDTO projectDTO);

    @Delete("delete from t_project where projectId=#{projectId}")
    int removeProject(Long projectId);

    @Select("select * from t_project where projectId=#{projectId}")
    ProjectDTO getProjectById(Long projectId);
}
