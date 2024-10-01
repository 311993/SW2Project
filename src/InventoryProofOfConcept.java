import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class representing a proof of concept implementation for an Inventory
 * component.
 *
 * @author David Stuckey
 */
public class InventoryProofOfConcept {

    /** The primary representation variable. */
    private ArrayList<Item> slots;

    /**
     * Constructs an Inventory with no slots. This should never be called from
     * outside the other constructor of this class so it is made private.
     */
    private InventoryProofOfConcept() {
        this.slots = new ArrayList<Item>();
    }

    /**
     * Constructs an Inventory with {@code size} slots.
     *
     * @param size
     *            the number of slots in {@code this}
     */
    public InventoryProofOfConcept(int size) {
        this();

        for (int i = 0; i < size; i++) {
            this.slots.add(new Item());
        }
    }

    /******* Kernel Methods *******/

    /**
     * Adds the Item {@code item} to slot if it is not already occupied by a
     * distinct Item. If the same Item is at the destination, they are stacked
     * (their count is summed).
     *
     * @param slot
     *            the slot at which to add the item
     * @param item
     *            the item to add at the destination
     * @return true if the item was successfully placed, and false otherwise
     */
    public boolean addItem(int slot, Item item) {

        Item dest = this.slots.get(slot);
        boolean placed = true;

        if (dest.isEmpty()) {
            this.slots.set(slot, item);
        } else if (dest.equals(item)) {
            dest.putTag(Item.COUNT,
                    dest.tagValue(Item.COUNT) + item.tagValue(Item.COUNT));
        } else {
            placed = false;
        }

        return placed;
    }

    /**
     * Remove the Item at {@code slot}. Will return an empty item if that slot
     * is empty.
     *
     * @param slot
     *            the position at which to remove an Item
     * @return the Item at {@code slot}
     */
    public Item removeItem(int slot) {

        Item removed = this.slots.remove(slot);
        this.slots.add(slot, new Item());

        return removed;
    }

    /**
     * Returns position of the first slot in the Inventory after position
     * {@code pos} at which an Item called {@code name} exists.
     *
     * @param name
     *            the name of the item to find
     * @param pos
     *            the starting position
     * @return the index of the first such slot, or -1 if there are none
     *
     */
    public int nextIndexOf(String name, int pos) {
        int i = pos;
        boolean hasItem = false;

        while (!hasItem && i < this.slots.size()) {
            hasItem = this.slots.get(i).getName().equals(name);
            i++;
        }

        if (!hasItem) {
            i = -1;
        }

        return i;
    }

    /**
     * Returns the total number of slots in this inventory.
     *
     * @return the number of slots in the inventory
     */
    public int size() {
        return this.slots.size();
    }

    /******* Secondary Methods *******/

    /**
     * Returns the item at a particular slot.
     *
     * @param slot
     *            the slot to get the item from
     * @return the Item at {@code slot}
     */
    public Item getItem(int slot) {
        Item removed = this.removeItem(slot);
        this.addItem(slot, removed);
        return removed;
    }

    /**
     * Swaps the items in 2 slots of {@code this}.
     *
     * @param slot1
     *            the first slot
     * @param slot2
     *            the second slot
     */
    public void swapItems(int slot1, int slot2) {

        Item removed1 = this.removeItem(slot1);
        Item removed2 = this.removeItem(slot2);

        this.addItem(slot1, removed2);
        this.addItem(slot2, removed1);
    }

    /**
     * Swaps Items between slots in 2 Inventories.
     *
     * @param src
     *            the Inventory to swap Items with
     *
     * @param srcSlot
     *            the slot in {@code src}
     * @param destSlot
     *            the slot in {@code this}
     */
    public void swapItems(InventoryProofOfConcept src, int srcSlot,
            int destSlot) {

        Item srcRemoved = src.removeItem(srcSlot);
        Item destRemoved = this.removeItem(destSlot);

        src.addItem(srcSlot, destRemoved);
        this.addItem(destSlot, srcRemoved);

    }

    /**
     * Transfers an Item from another inventory to this one.
     *
     * @param src
     *            the Inventory to transfer from
     *
     * @param srcSlot
     *            the slot in {@code src} to transfer from
     * @param destSlot
     *            the slot in {@code this} to place the item at
     * @return true if the Item is successfully placed, or false otherwise
     */
    public boolean transferItem(InventoryProofOfConcept src, int srcSlot,
            int destSlot) {

        Item srcRemoved = src.removeItem(srcSlot);

        boolean placed = this.addItem(destSlot, srcRemoved);

        if (!placed) {
            src.addItem(srcSlot, srcRemoved);
        }

        return placed;

    }

    /**
     * Returns position of the first slot in the Inventory at which {@code item}
     * could be added to the Inventory, or -1 if no such slot exists. The method
     * will attempt to stack items before placing in an empty slot.
     *
     * @param item
     *            the item to be placed
     * @param maxStack
     *            the maximum stack size desired, or 0 if you want unbounded
     *            stacking
     * @return the index of the first such slot, or -1 if there are none
     *
     */
    public int nextPlacement(Item item, int maxStack) {

        int pos = -1;
        int checkAt = 0;
        boolean doneCheckingStacks = false;

        //Try to stack the Item with others of its kind
        while (!doneCheckingStacks) {

            pos = this.nextIndexOf(item.getName(), checkAt);

            //If pos >= 0 a potential stack has been found
            if (pos >= 0) {

                //Make sure the stack is not full
                if (this.getItem(pos).tagValue(Item.COUNT) < maxStack) {
                    doneCheckingStacks = true;
                } else {
                    checkAt = pos;
                }

            } else {
                doneCheckingStacks = true;
            }
        }

        //If no viable stack is found, start a new one if possible
        if (pos < 0) {
            pos = this.nextIndexOf(Item.EMPTY_NAME, 0);
        }

        return pos;
    }

    /** Representation of a single item in an inventory. */
    public static final class Item {

        /**
         * Constant for default count tag. An item is guaranteed to have this
         * tag.
         */
        public static final String COUNT = "count";
        /** Constant for empty item name. */
        public static final String EMPTY_NAME = "";

        /** The name of this item, which serves as its primary identifier. */
        private String name;

        /**
         * Tags denoting the properties of the item, with integer values when
         * appropriate.
         */
        private Map<String, Integer> tags;

        /** Constructs an empty Item. */
        public Item() {
            this(EMPTY_NAME, 0);
        }

        /**
         * Constructs a named(non-empty) Item with count 1.
         *
         * @param name
         *            a String identifier for the Item.
         */
        public Item(String name) {
            this(name, 1);
        }

        /**
         * Constructs a named(non-empty) Item with count n.
         *
         * @param name
         *            a String identifier for the Item.
         * @param count
         *            the number of this Item.
         */
        public Item(String name, int count) {
            this.tags = new TreeMap<String, Integer>();
            this.name = name;
            this.tags.put("count", count);
        }

        /**
         * Returns whether this is an empty Item.
         *
         * @return whether {@code this} is empty.
         */
        public boolean isEmpty() {
            return this.name.equals(EMPTY_NAME);
        }

        /**
         * Returns the name/identifier of this Item.
         *
         * @return the name of {@code this}.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Returns whether this Item has the tag {@code tag}.
         *
         * @param tag
         *            the tag to check {@code this} for.
         *
         * @return true if the Item has the tag {@code tag} and false otherwise.
         */
        public boolean hasTag(String tag) {
            return this.tags.containsKey(tag);
        }

        /**
         * If the tag is not already present, adds the {@code (tag,value)} pair
         * to this Item, otherwise updates the value associated with the tag.
         *
         * @param tag
         * @param value
         */
        public void putTag(String tag, int value) {
            this.tags.put(tag, value);
        }

        /**
         * Removes {@code tag} from this Item.
         *
         * @param tag
         *            the tag to remove from {@code this}
         */
        public void removeTag(String tag) {
            assert !tag.equals(COUNT);
            this.tags.remove(tag);
        }

        /**
         * Returns the integer tag value associated with {@code tag}.
         *
         * @requires {@code this} has tag {@code tag}.
         * @param tag
         *            The tag to get the associated value of.
         * @return The integer value associated with {@code tag}.
         */

        public int tagValue(String tag) {
            return this.tags.get(tag);
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            boolean equal = false;

            if (o.getClass().equals(this.getClass())) {
                equal = ((Item) (o)).name.equals(this.name);
            }

            return equal;
        }
    }
}

/** Utility class for proof of concept demonstration. */
final class Main {

    /** Private constructor to prevent instantiation. */
    private Main() {
    }

    /**
     * Main method for proof of concept demonstration.
     *
     * @param args
     *            The command line arguments
     */
    public static void main(String... args) {

        /******* Demo 1: Collating items from one Inventory to another. *******/
        System.out.println(
                "Demo 1: Collating items from one Inventory to another.\n");

        //Construct demo vars
        final int ten = 10;
        InventoryProofOfConcept inv1 = new InventoryProofOfConcept(ten);
        InventoryProofOfConcept inv2 = new InventoryProofOfConcept(1);

        //Add items to slots of first inventory
        System.out.println("Items in Inventory 1:");

        for (int i = 0; i < inv1.size(); i++) {

            //Create Items with alternating names and random counts
            String name = "Gravel";

            if (i % 2 == 0) {
                name = "Food";
            }

            InventoryProofOfConcept.Item temp = new InventoryProofOfConcept.Item(
                    name, (int) (Math.random() * ten) + 1);
            inv1.addItem(i, temp);

            //Display each added item in terminal
            System.out.printf("%s : %d, \n", inv1.getItem(i).getName(), inv1
                    .getItem(i).tagValue(InventoryProofOfConcept.Item.COUNT));
        }

        //Send only "Gravel" items to 2nd inventory
        for (int i = 0; i < inv1.size(); i++) {
            if (inv1.getItem(i).getName().equals("Gravel")) {
                inv2.transferItem(inv1, i, 0);
            }
        }

        //Show results
        System.out.println();
        System.out.println("Gravel Sent to Inventory 2:");
        System.out.printf("%s : %d\n", inv2.getItem(0).getName(),
                inv2.getItem(0).tagValue(InventoryProofOfConcept.Item.COUNT));

        System.out.println();
        System.out.println("Items in Inventory 1:");
        for (int i = 0; i < inv1.size(); i++) {
            System.out.printf("%s : %d, \n", inv1.getItem(i).getName(), inv1
                    .getItem(i).tagValue(InventoryProofOfConcept.Item.COUNT));
        }

    }
}