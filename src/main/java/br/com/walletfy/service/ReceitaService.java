package br.com.walletfy.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.ReceitaRequestDTO;
import br.com.walletfy.dto.ReceitaResponseDTO;
import br.com.walletfy.entity.Categoria;
import br.com.walletfy.entity.FormaPagamento;
import br.com.walletfy.entity.Receita;
import br.com.walletfy.entity.StatusReceita;
import br.com.walletfy.entity.Usuario;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.mapper.ReceitaMapper;
import br.com.walletfy.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceitaService {

	private final ReceitaRepository receitaRepository;
	private final ReceitaMapper receitaMapper;
	private final UsuarioService usuarioService;
	private final CategoriaService categoriaService;
	private final FormaPagamentoService formaPagamentoService;
	private final StatusReceitaService statusReceitaService;
	
	public ReceitaResponseDTO cadastrar(Long usuarioId, ReceitaRequestDTO dto) {
		Usuario usuario = this.usuarioService.getReference(usuarioId);
		Categoria categoria = this.categoriaService.getReference(dto.getCategoriaId());
		StatusReceita status = this.statusReceitaService.getReference(dto.getStatusId());
		FormaPagamento formaPagamento = this.formaPagamentoService.getReference(dto.getFormaPagamentoId());
		
		Receita receita = Receita.builder()
				.usuario(usuario)
				.categoria(categoria)
				.formaPagamento(formaPagamento)
				.status(status)
				.descricao(dto.getDescricao())
				.valor(dto.getValor())
				.dataReceita(dto.getDataReceita())
//				.recorrente(false)
				.ativo(true)
				.build();

		return this.receitaMapper.toResponseDTO(this.receitaRepository.save(receita));		
	}

	public List<ReceitaResponseDTO> listar(Long usuarioId, LocalDate dataInicio, LocalDate dataFim){
		this.usuarioService.buscarPorId(usuarioId);
		
		return this.receitaRepository.findByUsuarioIdAndDataReceitaBetween(usuarioId, dataInicio, dataFim)
				.stream()
				.map(r -> this.receitaMapper.toResponseDTO(r)).toList();
	}
	
	public ReceitaResponseDTO atualizar(Long usuarioId, ReceitaRequestDTO dto) {
		if(dto.getId() == null) {
			throw new RegraNegocioException("Receita inválida");
		}
		Receita receita = this.receitaRepository.findById(dto.getId()).orElseThrow(() -> new RegraNegocioException("Receita não encontrada"));
		Usuario usuario = this.usuarioService.getReference(usuarioId);
		if (receita.getUsuario().getId() != usuario.getId()) {
			new RegraNegocioException("Essa receita não pertence a esse usuario");
		}

		Categoria categoria = this.categoriaService.getReference(dto.getCategoriaId());
		StatusReceita status = this.statusReceitaService.getReference(dto.getStatusId());
		FormaPagamento formaPagamento = this.formaPagamentoService.getReference(dto.getFormaPagamentoId());

		receita.setCategoria(categoria);
		receita.setFormaPagamento(formaPagamento);
		receita.setStatus(status);
		receita.setDescricao(dto.getDescricao());
		receita.setValor(dto.getValor());
		receita.setDataReceita(dto.getDataReceita());
		receita.setAtivo(true);

		return this.receitaMapper.toResponseDTO(this.receitaRepository.save(receita));		
	}
	
	public void deletar(Long usuarioId, Long receitaId) {
		Usuario usuario = this.usuarioService.getReference(usuarioId);
		Receita receita = this.receitaRepository.findByIdAndUsuarioId(receitaId, usuario.getId()).orElseThrow(() -> new RegraNegocioException("Receita não encontrada"));
		
		this.receitaRepository.delete(receita);
	}

}
