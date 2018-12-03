package br.edu.ulbra.election.election.repository;

import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, Long> {
    Vote findByElection_Id(Long electionId);
    List<Vote> findAllByCandidateId(Long candidateId);
    List<Vote> findAllByElectionIdAndCandidateId(Long electionId, Long candidateId);
    List<Vote> findByElection_IdAndCandidateId(Long electionId, Long candidateId);
    List<Vote> findAllByElection(Election election);
    //List<Vote> findVotesByVoter(Long voterId);
    Vote findFirstByElection(Election election);
}

