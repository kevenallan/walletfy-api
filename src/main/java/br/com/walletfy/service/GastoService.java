package br.com.walletfy.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.walletfy.dto.GastoRequestDTO;
import br.com.walletfy.dto.GastoResponseDTO;
import br.com.walletfy.entity.Categoria;
import br.com.walletfy.entity.FormaPagamento;
import br.com.walletfy.entity.Gasto;
import br.com.walletfy.entity.StatusGasto;
import br.com.walletfy.entity.Usuario;
import br.com.walletfy.exception.RegraNegocioException;
import br.com.walletfy.mapper.GastoMapper;
import br.com.walletfy.repository.GastoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GastoService {

	private final GastoRepository gastoRepository;
	private final GastoMapper gastoMapper;
	private final UsuarioService usuarioService;
	private final CategoriaService categoriaService;
	private final FormaPagamentoService formaPagamentoService;
	private final StatusGastoService statusGastoService;
	
	public GastoResponseDTO cadastrar(Long usuarioId, GastoRequestDTO dto) {
		Usuario usuario = this.usuarioService.getReference(usuarioId);
		Categoria categoria = this.categoriaService.getReference(dto.getCategoriaId());
		FormaPagamento formaPagamento = this.formaPagamentoService.getReference(dto.getFormaPagamentoId());
		StatusGasto statusGasto = this.statusGastoService.getReference(dto.getStatusId());
		
		Gasto gasto =  Gasto.builder()
				.usuario(usuario)
				.categoria(categoria)
				.formaPagamento(formaPagamento)
				.descricao(dto.getDescricao())
				.valor(dto.getValor())
				.dataGasto(dto.getDataGasto())
				.dataVencimento(dto.getDataVencimento())
				.ativo(true)
				.status(statusGasto)
				.build();

		gasto = this.gastoRepository.save(gasto);
		
		return gastoMapper.toResponseDTO(gasto);
	}
	
	public List<GastoResponseDTO> listar(Long usuarioId, LocalDate dataInicio, LocalDate dataFim) {
		this.usuarioService.buscarPorId(usuarioId);

		return this.gastoRepository.findByUsuarioIdAndDataGastoBetween(usuarioId, dataInicio, dataFim)
		.stream()
		.map(gastoMapper::toResponseDTO)
		.toList();
	}
	
	public GastoResponseDTO detalhar(Long gastoId, Long usuarioId) {
		this.usuarioService.buscarPorId(usuarioId);
		
		return gastoMapper.toResponseDTO(this.gastoRepository.findByIdAndUsuarioId(gastoId, usuarioId).get());
	}
	
	public GastoResponseDTO atualizar(Long usuarioId, GastoRequestDTO dto) {
		Gasto gasto = this.gastoRepository.findById(dto.getId()).orElseThrow(() -> new RegraNegocioException("Gasto não encontrado"));
		Usuario usuario = this.usuarioService.getReference(usuarioId);
		if (gasto.getUsuario().getId() != usuario.getId()) {
			new RegraNegocioException("Esse gasto não pertence a esse usuario");
		}

		Categoria categoria = this.categoriaService.getReference(dto.getCategoriaId());
		FormaPagamento formaPagamento = this.formaPagamentoService.getReference(dto.getFormaPagamentoId());
		StatusGasto statusGasto = this.statusGastoService.getReference(dto.getStatusId());
		
		 gasto.setCategoria(categoria);
		 gasto.setFormaPagamento(formaPagamento);
		 gasto.setDescricao(dto.getDescricao());
		 gasto.setValor(dto.getValor());
		 gasto.setDataGasto(dto.getDataGasto());
		 gasto.setDataVencimento(dto.getDataVencimento());
//		 gasto.setAtivo(true);
		 gasto.setStatus(statusGasto);

		gasto = this.gastoRepository.save(gasto);
		
		return gastoMapper.toResponseDTO(gasto);
	}
	
	public void deletar(Long usuarioId, Long gastoId) {
		Gasto gasto = this.gastoRepository.findById(gastoId).orElseThrow(() -> new RegraNegocioException("Gasto não encontrado"));
		Usuario usuario = this.usuarioService.getReference(usuarioId);
		if (gasto.getUsuario().getId() != usuario.getId()) {
			new RegraNegocioException("Esse gasto não pertence a esse usuario");
		}
		this.gastoRepository.deleteById(gasto.getId());
	}
}
