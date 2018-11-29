package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.client.CandidateClientService;
import br.edu.ulbra.election.election.client.VoterClientService;
import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final ModelMapper modelMapper;
    private final ElectionRepository electionRepository;
    private final VoterClientService voterClientService;
    private final CandidateClientService candidateClientService;

    private static final String MESSAGE_INVALID_ELECTION_ID = "Invalid id";
    private static final String MESSAGE_ELECTION_NOT_FOUND = "Election not found";
    private static final String MESSAGE_VOTES_NOT_FOUND = "Votes not found";

    @Autowired
    public VoteService(VoteRepository voteRepository, ModelMapper modelMapper, ElectionRepository electionRepository, VoterClientService voterClientService, CandidateClientService candidateClientService){
        this.voteRepository = voteRepository;
        this.modelMapper = modelMapper;
        this.electionRepository = electionRepository;
        this.voterClientService = voterClientService;
        this.candidateClientService = candidateClientService;
    }

    public GenericOutput electionVote(VoteInput voteInput){
        Election election = validateInput(voteInput.getElectionId(), voteInput);
        Vote vote = new Vote();
        vote.setElection(election);
        vote.setVoterId(voteInput.getVoterId());

        if (voteInput.getCandidateNumber() == null){
            vote.setBlankVote(true);
        } else {
            vote.setBlankVote(false);
        }
        // TODO: Validate null candidate
        int achou = 0;
        List<CandidateOutput> candidateOutput = candidateClientService.getAll();
        for (CandidateOutput candidate : candidateOutput) {
            System.out.println("TESTE "+ candidate.getNumberElection());
            System.out.println("TESTE "+ voteInput.getCandidateNumber());

            if(candidate.getNumberElection().equals(voteInput.getCandidateNumber())){
                System.out.println("####ACHOU...");
                vote.setCandidateId(candidate.getId());
                vote.setNullVote(false);
                achou++;
                break;
            }
        }

        if(achou == 0){
            vote.setNullVote(true);
        }


        voteRepository.save(vote);

        GenericOutput output = new GenericOutput("OK");
        return modelMapper.map(output, GenericOutput.class);
    }

    public GenericOutput multiple(List<VoteInput> voteInputList){
        for (VoteInput voteInput : voteInputList){
            this.electionVote(voteInput);
        }
        return new GenericOutput("OK");
    }

    public Election validateInput(Long electionId, VoteInput voteInput){
        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException("Invalid Election");
        }
        if (voteInput.getVoterId() == null){
            throw new GenericOutputException("Invalid Voter");
        }
        try{
            voterClientService.getById(voteInput.getVoterId());
        }catch (FeignException e){
            if (e.status() == 500) {
                throw new GenericOutputException("Invalid Voter");
            }
        }
        // TODO: Validate voter

        return election;
    }
}

