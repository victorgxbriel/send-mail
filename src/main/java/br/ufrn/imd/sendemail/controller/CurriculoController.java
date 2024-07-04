package br.ufrn.imd.sendemail.controller;

import br.ufrn.imd.sendemail.model.Curriculo;
import br.ufrn.imd.sendemail.service.CurriculoService;
import br.ufrn.imd.sendemail.service.FileDBService;
import br.ufrn.imd.sendemail.service.MailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/curriculo/")
public class CurriculoController {

    @Autowired
    private CurriculoService curriculoService;

    @Autowired
    private FileDBService fileDBService;

    @Autowired
    private MailService mailService;

    @RequestMapping("/")
    public List<Curriculo> getCurriculos() {
        return curriculoService.getCurriculos();
    }

    @PostMapping( value = "/", consumes = "multipart/form-data")
    public List<Curriculo> addCurriculo(
            HttpServletRequest request,
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("telefone") String telefone,
            @RequestParam("cargoDesejado") String cargoDesejado,
            @RequestParam("escolaridade") String escolaridade,
            @RequestParam("observacoes") String observacoes,
            @RequestParam("curriculo")MultipartFile file) {
        try {
            Curriculo curriculo = new Curriculo();
            curriculo.setEmail(email);
            curriculo.setNome(nome);
            curriculo.setEscolaridade(escolaridade);
            curriculo.setObservacoes(observacoes);
            curriculo.setTelefone(telefone);
            curriculo.setCargoDesejado(cargoDesejado);
            curriculo.setIpaddr(request.getRemoteAddr());
            curriculoService.addCurriculo(curriculo);
            String subject = "Envio Curriculo - " + curriculo.getNome();
            String text = "Olá, " + curriculo.getNome() + "! Obrigado pelo envio. Segue seus dados para confirmação:\n" +
                    "Número: " + curriculo.getTelefone() + "\nCargo desejado: " + curriculo.getCargoDesejado() +
                    "\nEscolaridade: " + curriculo.getEscolaridade() + "\nObservações: " + curriculo.getObservacoes() +
                    "\nSegue seu curriculo em anexo!";
            fileDBService.store(file);
            mailService.senEmailWithAttachment(curriculo.getEmail(), subject, text, file);
            return this.getCurriculos();
        } catch (IOException exception) {
            return null;
        }
    }
}
