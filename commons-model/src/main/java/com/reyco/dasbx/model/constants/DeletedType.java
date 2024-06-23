package com.reyco.dasbx.model.constants;

public enum DeletedType {
	DELETE((byte)1),
	NOMAL((byte)0);
	private Byte deleted;
	
	DeletedType(Byte deleted) {
		this.deleted = deleted;
	}
	public static DeletedType getDeletedType(Byte deleted) {
		for (DeletedType deletedType : DeletedType.values()) {
			if (deletedType.deleted == deleted) {
				return deletedType;
			}
		}
		return null;
	}
	public Byte getDeleted() {
		return deleted;
	}
	public void setDeleted(Byte deleted) {
		this.deleted = deleted;
	}
}