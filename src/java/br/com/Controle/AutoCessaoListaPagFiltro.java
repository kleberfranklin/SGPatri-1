/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.Controle;


import br.com.Modelo.AutoCessaoValidacao;
import br.com.Modelo.AutoCessaoValidacaoDAO;
import br.com.Modelo.Logica;
import br.com.Utilitario.Transformar;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author d732229
 */
public class AutoCessaoListaPagFiltro implements Logica{
@Override    
    public String executa(HttpServletRequest req,
                     HttpServletResponse res) throws Exception {
        
        AutoCessaoValidacaoDAO autoDAO = new AutoCessaoValidacaoDAO();
       
/**
*  Atributos: 
*  pg = número da página atual 
*  pi = número da página inicial
*  pf = número da página final
*  qtdRegistro = quantidade de registro geral
*  qtdPg = número de quantidade de páginas 
*  qtdLinha = número de quantidade de linhas por página
*  maxPg = quantidade de paginação a ser exibida (1, 2 , 3 ...)
*  sobraMaxPg = auxiliar para o calculdo da quantidade de pagina
* */        
        
        int pg, pi, pf, qtdRegistro, qtdPg, sobraMaxPg=0, offset, tpCessao=0;
        int qtdLinha = 6;
        int maxPg = 10;
        
        String pgS, piS, pfS, ter;
        String qTpcessao, qAC, qProcesso, qCessionario, qCedente, qEndereco, qCroqui, qVigor;
        
//Carregando atributos com a informações do formlário.           
        pgS = req.getParameter("pg");
        piS = req.getParameter("pi");
        pfS = req.getParameter("pf");
        
        qTpcessao = req.getParameter("qTpcessao");
        qProcesso = req.getParameter("qProcesso");
        qAC = req.getParameter("qAC");
        qCessionario = req.getParameter("qCessionario");
        qCedente = req.getParameter("qCedente");
        qEndereco = req.getParameter("qEndereco");
        qCroqui = req.getParameter("qCroqui");
        qVigor = req.getParameter("qVigor");
        ter = req.getParameter("ter");




//Validação dos atributos carregdos com as informações do formulário.         
        if (qTpcessao == null || qTpcessao.equals("")){
            qTpcessao ="";
        }else{
            tpCessao = Integer.parseInt(qTpcessao);
        }
        
        if (qAC == null){
            qAC ="";
        }
        
        if (qProcesso == null){
            qProcesso ="";
        }
        
        if (null == qCessionario  || qCessionario.equals("") ){
            qCessionario ="";
        }
        else{
            qCessionario = Transformar.removeAccents(Transformar.utf8(qCessionario)).toUpperCase().trim();
        }
        
        if (null == qCedente  || qCedente.equals("") ){
            qCedente ="";
        }
        else{
            qCedente = Transformar.removeAccents(Transformar.utf8(qCedente)).toUpperCase().trim();
        }
        if (null == qEndereco  || qEndereco.equals("") ){
            qEndereco ="";
        }
        else{
            qEndereco = Transformar.removeAccents(Transformar.utf8(qEndereco)).toUpperCase().trim();
        }
        
        if (null == qCroqui  || qCroqui.equals("") ){
            qCroqui ="";
        }
        else{
            qCroqui = Transformar.removeAccents(qCroqui).toUpperCase().trim();
        }
        
        
        if (qVigor == null){
            qVigor="";
        }
        
        if (pgS == null) {
            pg = 0;
        } else {
            pg = Integer.parseInt(pgS);
        }
        
        if (piS == null) {
            pi = 0;
        } else {
            pi = Integer.parseInt(piS);
        }
        
        if (pfS == null) {
            pf = 0;
        } else {
            pf = Integer.parseInt(pfS);
        }
      
        
//Carregando a quantidade de registro para calculdo da quantidade de paginas     
//        if ("".equals(ter) || null == ter){
            qtdRegistro = autoDAO.qtdAutoPesquisa(qTpcessao, qAC, qProcesso, qCessionario, qCedente, qEndereco, qCroqui, qVigor);
            qtdPg = qtdRegistro / qtdLinha;
//        }else{
//            qtdRegistro = autoDAO.qtdAutoCessaoPesquisaTerceiro(qTpcessao, qAC, qProcesso, qCessionario, qEndereco, qCroqui, qVigor);
//            qtdPg = qtdRegistro / qtdLinha;   
//        }
////Logica da paginação         
        if ((qtdRegistro % qtdLinha) != 0) {
            qtdPg = qtdPg+1;
        }

        if (qtdPg < maxPg) {
            maxPg = qtdPg;
        }
        if (qtdPg > maxPg) {
            sobraMaxPg = qtdPg - maxPg;
            if (sobraMaxPg > maxPg){
                sobraMaxPg = maxPg;  
            }
        }

        if (pg == 0) {
            pg = 1;
        }
        if (pf == 0) { 
            pf = maxPg;
        }
        if (pg == pf && pf != qtdPg && pf < qtdPg) {
               pi = pf;
               pf = pf + sobraMaxPg;
        } else if (pg == pi){
            pi = pi - maxPg;
            pf = pf - sobraMaxPg;
            pg = pg - 1;
        }
        offset = ((pg * qtdLinha)- qtdLinha);
        
//Populando o objeto lista 
//        List<AutoCessaoValidacao> listAuto;
//        if ("".equals(ter) || null == ter){
          List<AutoCessaoValidacao>  listAuto = new AutoCessaoValidacaoDAO().listPagFiltroPesquisa(qTpcessao, qAC, qProcesso, qCessionario, qCedente, qEndereco, qCroqui, qVigor, qtdLinha, offset);
//        }else{
//            listAuto = new AutoCessaoAntigoDAO().listPagFiltroTerceiro(qTpcessao, qAC, qProcesso, qCessionario, qEndereco, qCroqui, qVigor, qtdLinha, offset);
//        }

            qCessionario = URLEncoder.encode(qCessionario,"UTF-8");
//        qCedente = URLEncoder.encode(qCedente,"UTF-8");
//        qEndereco = URLEncoder.encode(qEndereco,"UTF-8");

        req.setAttribute("listAuto", listAuto);
                   
        return  "AutoCessaoLista.jsp?"
                +"pg="+pg
                +"&pi="+pi
                +"&pf="+pf
                +"&qtdPg="+qtdPg
                +"&totalRes="+qtdRegistro
                +"&qTpcessao="+qTpcessao
                +"&qAC="+qAC
                +"&qProcesso="+qProcesso
                +"&qCessionario="+qCessionario
                +"&qCedente="+qCedente
                +"&qEndereco="+qEndereco
                +"&qVigor="+qVigor
                +"&qCroqui="+qCroqui
                +"&ter"+ter;
 
    }
    
    
    
    
}
