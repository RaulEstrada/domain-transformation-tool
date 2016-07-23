package model.structure;

import java.util.List;

import model.associations.ManyToMany;
import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.associations.OneToOne;

public interface AnnotatedAttributes {
	List<Id> getId();
	void setEmbeddedId(EmbeddedId value);
	List<Basic> getBasic();
	List<Version> getVersion();
	List<ManyToOne> getManyToOne();
	List<OneToMany> getOneToMany();
	List<OneToOne> getOneToOne();
	List<ManyToMany> getManyToMany();
	List<ElementCollection> getElementCollection();
	List<Embedded> getEmbedded();
	List<Transient> getTransient();
}
