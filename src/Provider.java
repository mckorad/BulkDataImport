
public class Provider {
	
private String name; 
    
    private String identifier;
    
    public Provider(){}

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) 
    {
        this.identifier = identifier;
    }
    
    /**
     * @return the identifier
     */
    public String getIdentifier() 
    {
        return identifier;
    }
    
    /*@Override
    public String toString() {
        if (getPerson() != null)
            return getPerson().getPersonName().toString();
        else
            return getName();
    }*/
    
    /**
     * @see org.openmrs.BaseOpenmrsMetadata#getName()
     * @should return person full name if person is not null
     */
    
    public String getName() 
    {
        return this.name;
    }
    
    /**
     * @see org.openmrs.BaseOpenmrsMetadata#setName(java.lang.String)
     */
    
    public void setName(String name) 
    {
        this.name = name;
    }

}
