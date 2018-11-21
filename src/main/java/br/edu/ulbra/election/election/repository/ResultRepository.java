package br.edu.ulbra.election.election.repository;

import br.edu.ulbra.election.election.model.Result;
import org.springframework.data.repository.CrudRepository;

public interface ResultRepository  extends CrudRepository<Result, Long> {
}
