package integratedtoolkit.types.parameter;

import integratedtoolkit.types.annotations.parameter.DataType;
import integratedtoolkit.types.annotations.parameter.Direction;
import integratedtoolkit.types.annotations.parameter.Stream;
import integratedtoolkit.types.parameter.DependencyParameter;


public class ExternalObjectParameter extends DependencyParameter {

    /**
     * Serializable objects Version UID are 1L in all Runtime
     */
    private static final long serialVersionUID = 1L;

    private int hashCode;
    private Object value;


    public ExternalObjectParameter(Direction direction, Stream stream, String prefix, Object value, int hashCode) {
        super(DataType.EXTERNAL_PSCO_T, direction, stream, prefix);
        this.value = value;
        this.hashCode = hashCode;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return "ExternalObjectParameter with hash code" + hashCode;
    }

}
