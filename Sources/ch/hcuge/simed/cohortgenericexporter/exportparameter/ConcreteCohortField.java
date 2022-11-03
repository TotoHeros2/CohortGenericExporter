package ch.hcuge.simed.cohortgenericexporter.exportparameter;

public class ConcreteCohortField {

	private String _name;
	private Integer _duplicateCode;
	private CohortField _sourceField;

	public ConcreteCohortField(String name, Integer duplicateCode, CohortField sourceField) {
		this._name = name;
		this._duplicateCode = duplicateCode;
		this._sourceField = sourceField;
	}

	@Override
	public String toString() {
		return this._name +"', dc: '" + (this._duplicateCode == null ? "" : this._duplicateCode) + "', f: '"
				+ this._sourceField.name() + "'";
	}

	public String name() {
		return this._name;
	}

	public Integer duplicateCode() {
		return this._duplicateCode;
	}

	public CohortField sourceField() {
		return this._sourceField;
	}

	public String headerName() {
		return name().replace("$", "_").replace("£", "_").replace(" ", "_").replace("?", "_").replace("/", ".").replace("--", "_").toLowerCase();
	}
}
