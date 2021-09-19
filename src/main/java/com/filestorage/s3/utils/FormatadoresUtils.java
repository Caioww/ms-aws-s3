package com.filestorage.s3.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class FormatadoresUtils {
	
	private FormatadoresUtils() {
		
	}
	
	public static String formatadorDataBR(Date data) {
		 SimpleDateFormat formatador = new SimpleDateFormat("dd-MM-yyyy");
		 return formatador.format(data).replace("-", "/");
		
	}
	
	public static String extensaoArquivo(String nomeArquivo) {
		return nomeArquivo.substring(nomeArquivo.indexOf(".")+1);
		
	}

}
