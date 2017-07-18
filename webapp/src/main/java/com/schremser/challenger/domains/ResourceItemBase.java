package com.schremser.challenger.domains;

import java.util.Date;

public abstract class ResourceItemBase {
	private String _id;
	private String _rev;
	private String i_id;
	private String name;
	private String owner;
	private Date created;

	public ResourceItemBase() { }

	public ResourceItemBase(ResourceItemBase source ) {
		i_id = source.i_id;
		name = source.name;
		owner = source.owner;
		created = source.created;
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( !( o instanceof ResourceItemBase ) ) return false;
		ResourceItemBase other = (ResourceItemBase) o;
		if( i_id != null ? !i_id.equals( other.i_id ) : other.i_id != null ) return false;
		if( name != null ? !name.equals( other.name ) : other.name != null ) return false;
    return owner != null ? owner.equals(other.owner) : other.owner == null;
  }

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public void setId(String id ) {
		i_id = id;
	}

	public String getId( ) {
		return i_id;
	}

	public void setName( String name ) {
		name = name;
	}

	public String getName( ) {
		return name;
	}

	public void setOwner( String owner ) {
		owner = owner;
	}

	public String getOwner( ) {
		return owner;
	}

	public void setCreated( Date created ) {
		created = created;
	}

	public Date getCreated( ) {
		return created;
	}
	
}
