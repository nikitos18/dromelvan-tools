package org.dromelvan.tools.parser;

public class PlayerInformationParserObject extends ParserObject {

	public String name;
	public String nationality;
	public String dateOfBirth;
	public int height = 0;
	public int weight = 0;
	public int shirtNumber = 0;
	public String positions;
	public String fullName;

	public PlayerInformationParserObject() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getShirtNumber() {
		return shirtNumber;
	}

	public void setShirtNumber(int shirtNumber) {
		this.shirtNumber = shirtNumber;
	}

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

	public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
	public String toString() {
		return String.format("Name: %s Nationality: %s DOB: %s Height: %d Weight: %d Shirt number: %d Positions: %s Full Name: %s",
				getName(), getNationality(), getDateOfBirth(), getHeight(), getWeight(), getShirtNumber(), getPositions(), getFullName());
	}
}
