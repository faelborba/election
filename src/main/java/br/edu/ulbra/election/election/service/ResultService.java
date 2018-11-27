package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.client.CandidateClientService;
import br.edu.ulbra.election.election.client.PartyClientService;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.ElectionCandidateResultOutput;
import br.edu.ulbra.election.election.output.v1.ResultOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        resultOutput.setBlankVotes((long) blankVote);
        resultOutput.setElection(resultOutput.getElection());
        resultOutput.setNullVotes((long) nullVote);
        resultOutput.setTotalVotes((long) total);
        return modelMapper.map(resultOutput, ResultOutput.class);
    }

    public ElectionCandidateResultOutput getResultByCandidate(Long candidateId){
        List<Vote> votes = this.voteRepository.findAllByCandidateId(candidateId);
        //List<Vote> votes = this.voteRepository.findByElection_IdAndCandidateId(candidateId);
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

        System.out.println("#########\n"+ candidateOutput.get);

        System.out.println("##Teste## " + voteCandidate);
        electionCandidateResultOutput.setCandidate(candidateOutput);
        electionCandidateResultOutput.setTotalVotes((long)voteCandidate);
        electionCandidateResultOutput.getCandidate().setElectionOutput(candidateOutput.getElectionOutput());
        //electionCandidateResultOutput.getCandidate().setPartyOutput(partyClientService.getById(candidateOutput.getPartyOutput().getId()));
        return modelMapper.map(electionCandidateResultOutput, ElectionCandidateResultOutput.class);
    }

}
