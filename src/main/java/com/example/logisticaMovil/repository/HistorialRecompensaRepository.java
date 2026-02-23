package com.example.logisticaMovil.repository;

import com.example.logisticaMovil.model.HistorialRecompensa;
import com.example.logisticaMovil.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialRecompensaRepository extends JpaRepository<HistorialRecompensa, Long> {
    List<HistorialRecompensa> findByUsuario(Usuario usuario);
}
