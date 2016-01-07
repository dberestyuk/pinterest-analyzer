/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lue.pintrests;

import java.io.Serializable;

/**
 *
 * @author Lue Infoservices
 */
public class FinalResult implements Serializable, Comparable<FinalResult> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1081870635726111443L;

	private String url;
	private String link;
	private String note;
	private String pinId;
	private String imageUrl;
	private Double repins;
	private Double likes;

	public Double getRepins() {
		return repins;
	}

	public void setRepins(Double repins) {
		this.repins = repins;
	}

	public Double getLikes() {
		return likes;
	}

	public void setLikes(Double likes) {
		this.likes = likes;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPinId() {
		return pinId;
	}

	public void setPinId(String pinId) {
		this.pinId = pinId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int compareTo(FinalResult o) {

		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		if (o.getRepins() + o.getLikes() > this.getRepins() + this.getLikes()) {
			return BEFORE;
		} else if (o.getRepins() + o.getLikes() < this.getRepins()
				+ this.getLikes()) {
			return AFTER;
		} else {
			return EQUAL;
		}
	}

}
