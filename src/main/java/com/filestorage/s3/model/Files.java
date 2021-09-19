package com.filestorage.s3.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class Files implements Serializable{
	

	private static final long serialVersionUID = 1L;

	private String nomeArquivo;
	private String dataModificacao;
	private String extensao;
	private String novoArquivo;
	
	
	public Files(String key, String lastModified, String tipoArquivo) {
		this.nomeArquivo = key;
		this.dataModificacao = lastModified;
		this.extensao = tipoArquivo;
		
	}
}
