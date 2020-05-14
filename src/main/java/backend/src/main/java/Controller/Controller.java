package Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Beans.Mbean;
import DTO.Point;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value="Controlador de Rotas")
@RestController
public class Controller {

	Mbean mbean = new Mbean();

	@ApiOperation(value="Administra Rotas", notes="Carregamento assíncrono.")
	@RequestMapping(value = "/roteiros/carregar", method = RequestMethod.POST)
	public void carregarArquivo(
			@ApiParam(value="<pathCompleto>\nomeDoArquivo") 
			@RequestBody String pathNomeArquivo) {

		mbean.carregarDados(pathNomeArquivo);
	}

	@ApiOperation(value="Consulta Roteiro", notes="Consulta a Roteiros.")
	@RequestMapping(value = "/roteiros/{roteiro}", method = RequestMethod.POST)
	public ResponseEntity<String> consultarRoteiro( 
			@PathVariable("roteiro") String roteiro) {
		
		String retorno = Mbean.buscarResultado(roteiro.toUpperCase());
		
		if(!"404".equals(retorno)) {
			return new ResponseEntity<String>(retorno, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Route not found!", HttpStatus.NOT_FOUND);
	}
	
	@ApiOperation(value="Insere produtos", notes="Insere um novo reoteiro")
	@RequestMapping(value = "/roteiros/{roteiro}/insert", method = RequestMethod.PUT)
	public ResponseEntity<String> atualizarProduto( 
			@PathVariable("roteiro") String roteiroParm) {
		
		String[] roteiro = roteiroParm.toUpperCase().split(",");
		
		if(roteiro.length != 3 || roteiro[0].length() != 3 || roteiro[1].length() != 3 || roteiro[2].length() < 1 || Integer.parseInt(roteiro[2]) < 1) {
			return new ResponseEntity<String>("Esperado: XXX,XXX,999", HttpStatus.BAD_REQUEST);
		}
		mbean.inserirRota(roteiro[0], roteiro[1], Integer.parseInt(roteiro[2]));
		
		return new ResponseEntity<String>(roteiroParm + " Inserido!", HttpStatus.OK);
	}

	@ApiOperation(value="Verifica se o servico está ativo")
    @RequestMapping("/")
    public String ping() {
        return "Sorry! My answers are limited. You have to ask the right questions!";
    }

	@ApiOperation(value="Teste")
    @RequestMapping("/test")
    public String test() {
    	return "Testing 1, 2, 3!";
    }
}