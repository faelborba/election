package br.edu.ulbra.election.election.api.v1;

import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.service.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/v1/vote")
public class VoteApi {

    private final ElectionService electionService;

    @Autowired
    public VoteApi(ElectionService electionService){
        this.electionService = electionService;
    }

    @PutMapping("/{electionId}")
    public GenericOutput electionVote(@RequestBody VoteInput voteInput){
        return new GenericOutput("OK");
    }

    @PutMapping("/multiple")
    public GenericOutput multipleElectionVote(@RequestBody List<VoteInput> voteInputList){
        return new GenericOutput("OK");
    }
}
