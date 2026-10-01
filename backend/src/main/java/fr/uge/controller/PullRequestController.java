package fr.uge.controller;

import fr.uge.model.PullRequest;
import io.micronaut.http.annotation.Controller;

import java.util.Objects;

@Controller("pullrequest")
public class PullRequestController {
    private  final  Requetes requetes;
    public PullRequestController(Requetes requetes){
        Objects.requireNonNull(requetes);
        this.requetes = requetes;
        super();
    }
}
