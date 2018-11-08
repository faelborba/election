package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.ElectionInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElectionService {
    private final ElectionRepository electionRepository;
    private final ModelMapper modelMapper;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_ELECTION_NOT_FOUND = "Election not found";

    @Autowired
    public ElectionService(ElectionRepository electionRepository, ModelMapper modelMapper){
        this.electionRepository = electionRepository;
        this.modelMapper = modelMapper;
    }

    public List<ElectionOutput> getAll(){
        Type electionOutputListType = new TypeToken<List<ElectionOutput>>(){}.getType();
        return modelMapper.map(electionRepository.findAll(), electionOutputListType);
    }

    public ElectionOutput create(ElectionInput electionInput){
        this.validateInput(electionInput);
        Election election = modelMapper.map(electionInput, Election.class);
        election = electionRepository.save(election);
        return modelMapper.map(election,ElectionOutput.class);
    }

    public List<ElectionOutput> getByYear(Integer electionYear){
        Type electionOutputListType = new TypeToken<List<ElectionOutput>>(){}.getType();
        return modelMapper.map(electionRepository.findByYear(electionYear), electionOutputListType);
    }

    public ElectionOutput getById(Long electionId){

        if(electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput update(Long electionId, ElectionInput electionInput){
        if(electionId == null){
          throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(electionInput);
        Election election = electionRepository.findById(electionId).orElse(null);

        if(election==null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }
        election.setDescription(electionInput.getDescription());
        election.setStateCode(electionInput.getStateCode());
        election.setYear(electionInput.getYear());
        election = electionRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }
    public GenericOutput delete(Long electionId){
        if(electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = electionRepository.findById(electionId).orElse(null);
        if(election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        electionRepository.delete(election);

        return new GenericOutput("Election deleted");
    }


    private void validateInput(ElectionInput electionInput){
        if(electionInput.getYear() == null){
            throw new GenericOutputException("Invalid year");
        }

        if(validateYear(electionInput.getYear())){
            throw new GenericOutputException("Invalid year");
        }

        if(StringUtils.isBlank(electionInput.getDescription())){
            throw new GenericOutputException("Invalid Description");
        }
        if(validateDescription(electionInput.getDescription())){
            throw new GenericOutputException("Invalid Description");
        }

        if(StringUtils.isBlank(electionInput.getStateCode())){
            throw new GenericOutputException("Invalid State Code");
        }

        if(validaSatateCode(electionInput.getStateCode())){
            throw new GenericOutputException("Invalid State Code");
        }
    }
    private boolean validaSatateCode(String stateCode){
        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("AC");
        stateList.add("AL");
        stateList.add("AP");
        stateList.add("BA");
        stateList.add("CE");
        stateList.add("DF");
        stateList.add("ES");
        stateList.add("GO");
        stateList.add("MA");
        stateList.add("MT");
        stateList.add("MS");
        stateList.add("MG");
        stateList.add("PA");
        stateList.add("PB");
        stateList.add("PR");
        stateList.add("PE");
        stateList.add("PI");
        stateList.add("RJ");
        stateList.add("RN");
        stateList.add("RS");
        stateList.add("RO");
        stateList.add("RR");
        stateList.add("SC");
        stateList.add("SE");
        stateList.add("TO");
        stateList.add("BR");// vai brasiliam
        if(!stateList.contains(stateCode)){
            return true;
        }
        return false;
    }
    private boolean validateDescription(String description){
        if(description.length() < 5){// verificando a quantidade de caracteres
            return true;
        }
        return false;
    }
    private boolean validateYear(Integer year){
        if (year < 2000 || year >2200){
            return true;
        }
        return false;
    }
}
