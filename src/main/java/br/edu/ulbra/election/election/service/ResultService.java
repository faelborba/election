package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Result;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.ResultOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.ResultRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ResultService {
    private final ResultRepository resultRepository;
    private final ModelMapper modelMapper;
    private final ElectionRepository electionRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository, ModelMapper modelMapper, ElectionRepository electionRepository, VoteRepository voteRepository){
        this.resultRepository = resultRepository;
        this.modelMapper =modelMapper;
        this.electionRepository = electionRepository;
        this.voteRepository = voteRepository;
    }

    private static final String MESSAGE_INVALID_ELECTION_ID = "Invalid id";
    private static final String MESSAGE_ELECTION_NOT_FOUND = "Election not found";
    private static final String MESSAGE_VOTES_NOT_FOUND = "Votes not found";

    public List<ResultOutput> getAll(){
        Type resultOutputListType = new TypeToken<List<ResultOutput>>(){}.getType();
        return modelMapper.map(resultRepository.findAll(), resultOutputListType);
    }

    public ResultOutput getResultByElection(Long electionId){
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ELECTION_ID);
        }
        Election election = electionRepository.findById(electionId).orElse(null);

        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        List<Vote> votes = (List<Vote>) voteRepository.findAllByElectionId(electionId);

        /*if(votes.size() > 0){
            throw new GenericOutputException(MESSAGE_VOTES_NOT_FOUND);
        }*/

        Result result = new Result();
        int blankVote = 0, nullVote = 0, totalVotes = 0;
        for (Vote vote : votes) {
            if(vote.getBlankVote() == true){
                blankVote++;
            }
            if(vote.getNullVote() == true){
                nullVote++;
            }
            totalVotes++;
        }

        result.setElection(modelMapper.map(election, ElectionOutput.class));
        result.setBlankVotes((long) blankVote);
        result.setNullVotes((long) nullVote);
        result.setTotalVotes((long) totalVotes);

        return modelMapper.map(result, ResultOutput.class);
    }

}
