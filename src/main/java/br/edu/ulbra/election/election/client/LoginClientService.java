package br.edu.ulbra.election.election.client;

import br.edu.ulbra.election.election.output.v1.VoterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class LoginClientService {
    private final LoginClient loginClient;
    @Autowired
    public LoginClientService(LoginClient loginClient){
        this.loginClient = loginClient;
    }

    @FeignClient(value="login-service", url="${url.login-service}")
    public interface LoginClient {
        @GetMapping("/v1/login/check/{token}")
        VoterOutput checkToken(@PathVariable(name = "token") String token);
    }

    public VoterOutput checkToken(String token){
        return this.loginClient.checkToken(token);
    }
}
