package dev.tanpn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.tanpn.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
}