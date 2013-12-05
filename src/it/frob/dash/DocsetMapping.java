package it.frob.dash;

/**
 * Mapping class for docsets.
 */
public class DocsetMapping {
	/**
	 * The entry to map.
	 */
	private String mEntry;

	/**
	 * The associated docset.
	 */
	private String mDocset;

	/**
	 * Default constructor.
	 */
	public DocsetMapping() {
		this("", "");
	}

	/**
	 * Specialised constructor for already known values.
	 *
	 * @param entry the entry to associate a docset to.
	 * @param docset the docset to associate.
	 */
	public DocsetMapping(String entry, String docset) {
		mEntry = entry;
		mDocset = docset;
	}

	/**
	 * Returns the mapped entry.
	 *
	 * @return the mapped entry.
	 */
	public String getEntry() {
		return mEntry;
	}

	/**
	 * Sets the mapped entry value.
	 *
	 * @param entry the entry to map.
	 */
	public void setEntry(String entry) {
		mEntry = entry;
	}

	/**
	 * Returns the associated docset.
	 *
	 * @return the associated docset.
	 */
	public String getDocset() {
		return mDocset;
	}

	/**
	 * Sets the associated docset.
	 *
	 * @param docset the docset to associate.
	 */
	public void setDocset(String docset) {
		mDocset = docset;
	}
}
