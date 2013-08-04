package topicpane;
public class CustomNumberClass<E> {
	private E num;
	public CustomNumberClass() {
		num = null;
	}
	public CustomNumberClass(E num) {
		this.num = num;
	}
	public void setValue(E num) {
		this.num = num;
	}
	public E getValue() {
		return num;
	}
}
