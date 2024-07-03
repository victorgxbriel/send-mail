package br.ufrn.imd.sendemail.controller;

import br.ufrn.imd.sendemail.model.Curriculo;
import br.ufrn.imd.sendemail.service.CurriculoService;
import br.ufrn.imd.sendemail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/curriculo/")
public class CurriculoController {

    @Autowired
    private CurriculoService curriculoService;

    @Autowired
    private MailService mailService;

    @RequestMapping("/")
    public List<Curriculo> getCurriculos() {
        return curriculoService.getCurriculos();
    }

    @PostMapping("/")
    public List<Curriculo> addCurriculo(@RequestBody Curriculo curriculo) {
        curriculoService.addCurriculo(curriculo);
        String subject = "Envio Curriculo - " + curriculo.getNome();
        String text = "Olá, " + curriculo.getNome() + "! Obrigado pelo envio. Segue seus dados para confirmação:\n" +
                "Número: " + curriculo.getTelefone() + "\nCargo desejado: " + curriculo.getCargoDesejado() +
                "\nEscolaridade: " + curriculo.getEscolaridade() + "\nObservações: " + curriculo.getObservacoes() +
                "\nSegue seu curriculo em anexo!";
        mailService.senEmailWithAttachment(curriculo.getEmail(), subject, text, curriculo.getCurriculo());
        return this.getCurriculos();
    }
}
