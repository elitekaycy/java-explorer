package utils;

public class HuffmanNode implements Comparable<HuffmanNode> {

  char character;
  int frequency;
  HuffmanNode left, right;

  HuffmanNode(char character, int frequency) {
    this.character = character;
    this.frequency = frequency;
    this.left = null;
    this.right = null;
  }

  HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
    this.character = '\0';
    this.left = left;
    this.right = right;
  }

  @Override
  public int compareTo(HuffmanNode other) {
    return Integer.compare(this.frequency, other.frequency);
  }
}
