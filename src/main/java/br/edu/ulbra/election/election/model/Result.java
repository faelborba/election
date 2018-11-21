package br.edu.ulbra.election.election.model;

import br.edu.ulbra.election.election.output.v1.ElectionCandidateResultOutput;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class Result {

    @Column(nullable = false)
    private ElectionOutput election;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private List<ElectionCandidateResultOutput> candidates;

    @Column(nullable = true)
    private Long totalVotes;

    @Column(nullable = true)
    private Long blankVotes;

    @Column(nullable = true)
    private Long nullVotes;

    public ElectionOutput getElection() {
        return election;
    }

    public void setElection(ElectionOutput election) {
        this.election = election;
    }

    public List<ElectionCandidateResultOutput> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<ElectionCandidateResultOutput> candidates) {
        this.candidates = candidates;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Long getBlankVotes() {
        return blankVotes;
    }

    public void setBlankVotes(Long blankVotes) {
        this.blankVotes = blankVotes;
    }

    public Long getNullVotes() {
        return nullVotes;
    }

    public void setNullVotes(Long nullVotes) {
        this.nullVotes = nullVotes;
    }
}
