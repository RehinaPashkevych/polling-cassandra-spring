package com.example.polling.controllers;

import com.example.polling.entities.PollResult;
import com.example.polling.services.ResultService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/poll_results")
public class PollResultController {
    @Autowired
    private ResultService resultService;

    @GetMapping(value = "/{id}")
    private ResponseEntity<List<PollResult>> getResult(@Valid @PathVariable("id") UUID pollId) {
        ResultService.Results res = this.resultService.getResults(pollId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Final-Results", Boolean.toString(res.isActive()));
        return new ResponseEntity<List<PollResult>>(res.getResults(), headers, HttpStatus.OK) ;
    }
}
