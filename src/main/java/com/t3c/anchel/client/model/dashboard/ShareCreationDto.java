package com.t3c.anchel.client.model.dashboard;

import java.util.List;
import java.util.Set;

import com.t3c.anchel.client.model.IModel;

public class ShareCreationDto implements IModel {

	private static final long serialVersionUID = 1L;

	protected List<RecipientsDto> recipients;
	protected List<String> documents;
	protected Boolean creationAcknowledgement;
	protected String expirationDate;
	protected String subject;
	protected String message;
	protected String notificationDateForUSDA;
	protected Boolean enableUSDA;
	protected String sharingNote;
	private Set<String> mailingListUuid;

	@Override
	public String toString() {
		return "ShareCreationDto [recipients=" + recipients + ", documents=" + documents + ", creationAcknowledgement="
				+ creationAcknowledgement + ", expirationDate=" + expirationDate + ", subject=" + subject + ", message="
				+ message + ", notificationDateForUSDA=" + notificationDateForUSDA + ", enableUSDA=" + enableUSDA
				+ ", sharingNote=" + sharingNote + ", mailingListUuid=" + mailingListUuid + "]";
	}

	public List<RecipientsDto> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<RecipientsDto> recipients) {
		this.recipients = recipients;
	}

	public List<String> getDocuments() {
		return documents;
	}

	public void setDocuments(List<String> documents) {
		this.documents = documents;
	}

	public Boolean getCreationAcknowledgement() {
		return creationAcknowledgement;
	}

	public void setCreationAcknowledgement(Boolean creationAcknowledgement) {
		this.creationAcknowledgement = creationAcknowledgement;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNotificationDateForUSDA() {
		return notificationDateForUSDA;
	}

	public void setNotificationDateForUSDA(String notificationDateForUSDA) {
		this.notificationDateForUSDA = notificationDateForUSDA;
	}

	public Boolean getEnableUSDA() {
		return enableUSDA;
	}

	public void setEnableUSDA(Boolean enableUSDA) {
		this.enableUSDA = enableUSDA;
	}

	public String getSharingNote() {
		return sharingNote;
	}

	public void setSharingNote(String sharingNote) {
		this.sharingNote = sharingNote;
	}

	public Set<String> getMailingListUuid() {
		return mailingListUuid;
	}

	public void setMailingListUuid(Set<String> mailingListUuid) {
		this.mailingListUuid = mailingListUuid;
	}
}
