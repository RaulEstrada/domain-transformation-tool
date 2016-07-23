package input.classes.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.associations.MapKeyJoinColumn;

public class MapKeyJoinColumnsAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public List<MapKeyJoinColumn> createMapKeyJoinColumns(
			javax.persistence.MapKeyJoinColumns annotation) {
		javax.persistence.MapKeyJoinColumn[] mapKeyJoinColumnAnnotations =
				annotation.value();
		List<MapKeyJoinColumn> mapKeyJoinColumns = new ArrayList<>();
		for (javax.persistence.MapKeyJoinColumn mapKeyJoinColumnAnnotation :
			mapKeyJoinColumnAnnotations) {
			mapKeyJoinColumns.add(new MapKeyJoinColumnAnalyzer()
					.createMapKeyJoinColumn(mapKeyJoinColumnAnnotation));
		}
		return mapKeyJoinColumns;
	}
}
