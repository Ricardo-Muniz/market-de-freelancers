package br.com.nouva.freelancer.Service;

import java.util.List;

import br.com.nouva.freelancer.Model.BodySearch;
import br.com.nouva.freelancer.Model.ModelMoreProjects;
import br.com.nouva.freelancer.Model.ModelOverview;
import br.com.nouva.freelancer.Model.ModelUrls;
import br.com.nouva.freelancer.Model.Overview;
import br.com.nouva.freelancer.Model.Project;
import br.com.nouva.freelancer.Model.Urls;
import br.com.nouva.freelancer.Response.ResponseAddProject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceHome {

  @POST("/search")
  Call<List<ModelMoreProjects>> moreProjects(@Body BodySearch body);

  @POST("/overview")
  Call<List<ModelOverview>> seeOverview(@Body Overview key);

  @POST("/urls")
  Call<List<ModelUrls>> getUlrs(@Body Urls key);

  @POST("/save")
  Call<ResponseAddProject> addProject(@Body Project project);

}

