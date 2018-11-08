package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.VoteRepository;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final ModelMapper modelMapper;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_VOTE_NOT_FOUND = "Vote not found";

    @Autowired
    public VoteService(VoteRepository voteRepository, ModelMapper modelMapper){
        this.voteRepository = voteRepository;
        this.modelMapper = modelMapper;
    }

    public List<GenericOutput> multipleElectionVote(VoteInput voteInput){
        Type voteOutputListType = new TypeToken<List<GenericOutput>>(){}.getType();
        return modelMapper.map(voteRepository.findAll(), voteOutputListType);
    }

    public GenericOutput electionVote(Long electionId/*, VoteInput voteInput*/){
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        /*
        Vote vote =voteRepository.findById(electionId).orElse(null);
        if(vote == null){
            throw new GenericOutputException(MESSAGE_VOTE_NOT_FOUND);
        }

        vote.setCandidateId(voteInput.getCandidateId());
        vote.setElectionId(voteInput.getElectionId());
        vote.setVoterId(voteInput.getVoterId());
        vote =voteRepository.save(vote);*/
        return new GenericOutput("Comand OK");
    }

    private void validateInput(VoteInput voteInput){
        if(voteInput.getCandidateId() == null){
            throw new GenericOutputException("Invalid Candidate ID");
        }
        if(voteInput.getElectionId() == null){
            throw new GenericOutputException("Invalid Election ID");
        }
        if(voteInput.getVoterId() == null){
            throw new GenericOutputException("Invalid Voter ID");
        }
    }

}
