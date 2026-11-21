package br.ucsal.pokesal.servicos;

/**
 * Servico externo de notificacoes do torneio (Requisito Autoral 4).
 */
public interface ServicoNotificacao {

    /**
     * Envia uma mensagem de notificacao para um destinatario.
     *
     * @param destinatario Nome do treinador ou mural destinatario.
     * @param mensagem Conteudo da notificacao.
     */
    void notificar(String destinatario, String mensagem);
}
