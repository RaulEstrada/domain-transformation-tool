package model.structure;

import model.associations.ManyToMany;
import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.associations.OneToOne;

public interface ElementInRelationship {
	ManyToMany getCorrespondingManyToMany(String targetEntity, String mappedBy);
	OneToOne getCorrespondingOneToOne(String targetEntity, String mappedBy);
	ManyToOne getCorrespondingManyToOne(String targetEntity, String mappedBy);
	OneToMany getCorrespondingOneToMany(String targetEntity, String mappedBy);
}
