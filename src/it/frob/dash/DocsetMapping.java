package it.frob.dash;

/**
 * Mapping class for docsets.
 */
public class DocsetMapping {

	/**
	 * The type to map.
	 */
	private String type;

	/**
	 * The associated docset.
	 */
	private String docset;

	/**
	 * Default constructor.
	 */
	public DocsetMapping() {
		this("", "");
	}

	/**
	 * Specialised constructor for already known values.
	 *
	 * @param typeMapping the type to associate a docset to.
	 * @param docsetMapping the docset to associate.
	 */
	public DocsetMapping(String typeMapping, String docsetMapping) {
		type = typeMapping;
		docset = docsetMapping;
	}

	/**
	 * Returns the mapped type.
	 *
	 * @return the mapped type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the mapped type.
	 *
	 * @param typeMapping the type to map.
	 */
	public void setType(String typeMapping) {
		type = typeMapping;
	}

	/**
	 * Returns the associated docset.
	 *
	 * @return the associated docset.
	 */
	public String getDocset() {
		return docset;
	}

	/**
	 * Sets the associated docset.
	 *
	 * @param docsetMapping the docset to associate.
	 */
	public void setDocset(String docsetMapping) {
		docset = docsetMapping;
	}
}
