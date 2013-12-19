package com.lxs.oa.tour.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tour_detail_")
public class TourDetail implements Serializable {
	private Long id;
	private TourCommon common;
	private String name;
	private Long money;

	@Id
	@GeneratedValue
	@Column(name = "id_")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "common_id_")
	public TourCommon getCommon() {
		return common;
	}

	public void setCommon(TourCommon common) {
		this.common = common;
	}

	@Column(name = "name_")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "money_")
	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}
}
