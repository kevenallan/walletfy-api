package br.com.walletfy.service;

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
	
	public List<GastoResponseDTO> listar(Long usuarioId) {
		this.usuarioService.buscarPorId(usuarioId);

		return this.gastoRepository.findByUsuarioId(usuarioId)
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
		Categoria categoria = this.categoriaService.getReference(dto.getCategoriaId());
		FormaPagamento formaPagamento = this.formaPagamentoService.getReference(dto.getFormaPagamentoId());
		StatusGasto statusGasto = this.statusGastoService.getReference(dto.getStatusId());
		
		 gasto =  Gasto.builder()
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
}
