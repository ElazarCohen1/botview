package fr.uge.git;


import fr.uge.git.model.GitUserDto;

public interface GitProvider {
  GitUserDto getUser();

  void saveToken(String token);

  String getToken();

}
