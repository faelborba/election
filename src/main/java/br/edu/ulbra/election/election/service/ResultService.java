package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.client.CandidateClientService;
import br.edu.ulbra.election.election.client.PartyClientService;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.ElectionCandidateResultOutput;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.ResultOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResultService {
    private final ModelMapper modelMapper;
    private final ElectionRepository electionRepository;
    private final VoteRepository voteRepository;
    private final CandidateClientService candidateClientService;
    private final PartyClientService partyClientService;

    @Autowired
    public ResultService(ModelMapper modelMapper, ElectionRepository electionRepository, VoteRepository voteRepository, CandidateClientService candidateClientService, PartyClientService partyClientService){
        this.modelMapper = modelMapper;
        this.electionRepository = electionRepository;
        this.voteRepository = voteRepository;
        this.candidateClientService = candidateClientService;
        this.partyClientService = partyClientService;
    }

    public ResultOutput getResultByElection(Long electionId){
        ResultOutput resultOutput = new ResultOutput();
        Election election = this.electionRepository.findById(electionId).orElse(null);
        List<Vote> votes = this.voteRepository.findAllByElection(election);
        int nullVote = 0, blankVote = 0, total = 0;
        for (Vote vote : votes) {
            if (vote.getNullVote() == true){
                nullVote++;
            }
            if (vote.getBlankVote() == true){
                blankVote++;
            }
            total++;
        }
        /*votes = this.voteRepository.findAllByElectionIdAndCandidateId(electionId, candidateId);
        for (Vote vote : votes) {

        }*/
        List<CandidateOutput> candidateOutputs = candidateClientService.getAll();
        List<ElectionCandidateResultOutput> candidatesResult = new ArrayList<>();
        ElectionCandidateResultOutput electionCandidateResultOutput = new ElectionCandidateResultOutput();
        for (CandidateOutput candidate : candidateOutputs) {
            if(candidate.getElectionOutput().getId() == electionId){
                System.out.println("###achado: "+ electionId);
                electionCandidateResultOutput.setCandidate(candidate);
                electionCandidateResultOutput.setTotalVotes((long)total);
                candidatesResult.add(electionCandidateResultOutput);
            }
        }
        resultOutput.setCandidates(candidatesResult);
        ElectionOutput electionOutput = modelMapper.map (election, ElectionOutput.class);
        resultOutput.setElection(electionOutput);
        resultOutput.setBlankVotes((long) blankVote);
        resultOutput.setNullVotes((long) nullVote);
        resultOutput.setTotalVotes((long) total);
        return modelMapper.map(resultOutput, ResultOutput.class);
    }

    public CandidateOutput getCandidateOutput(Long candidateId){
        CandidateOutput candidateOutput = candidateClientService.getById(candidateId);
        return candidateOutput;
    }

    public ElectionCandidateResultOutput getResultByCandidate(Long candidateId){
        List<Vote> votes = this.voteRepository.findAllByCandidateId(candidateId);
        ElectionCandidateResultOutput electionCandidateResultOutput = new ElectionCandidateResultOutput();
        CandidateOutput candidateOutput = candidateClientService.getById(candidateId);
        int voteCandidate = 0;

        for (Vote vote : votes) {
            if(vote.getCandidateId() == candidateId){
                if(electionRepository.findById(vote.getElection().getId()) != null){
                    voteCandidate++;
                }
            }
        }

        System.out.println("##Teste## " + voteCandidate);
        electionCandidateResultOutput.setCandidate(candidateOutput);
        electionCandidateResultOutput.setTotalVotes((long)voteCandidate);
        electionCandidateResultOutput.getCandidate().setElectionOutput(candidateOutput.getElectionOutput());
        return modelMapper.map(electionCandidateResultOutput, ElectionCandidateResultOutput.class);
    }

}
