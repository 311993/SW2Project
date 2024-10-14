import components.standard.Standard;

/**
 * Inventory kernel component with primary methods.
 *
 * @author David Stuckey
 */
public interface InventoryKernel extends Standard<Inventory> {

    /**
     * Adds the Item {@code item} to {@code this} at {@code slot}. If the same
     * Item is at the destination, they are stacked (their count is summed). The
     * destination slot must be empty or hold an Item with the same name. The
     * Item must satisfy the tag restrictions imposed by {@code this}.
     *
     * @param slot
     *            the slot at which to add the Item
     * @param item
     *            the item to add at the destination
     * @updates this
     *
     * @requires <pre>
     * - 0 <= slot < |this|
     *- item is not null
     *- this[slot] = empty OR this[slot].name = item.name
     *- this.isAllowed(item)
     *</pre>
     * @ensures this[slot] = item
     *
     * @aliases this[slot] will be an alias of item
     */
    void addItem(int slot, Inventory.Item item);

    /**
     * Remove the Item at {@code slot}. Will return an empty Item if that slot
     * is empty.
     *
     * @param slot
     *            the position at which to remove an Item
     * @return the Item at {@code slot}
     *
     * @updates this
     *
     * @requires 0 <= slot < |this|
     *
     * @ensures this[slot] is empty
     */
    Inventory.Item removeItem(int slot);

    /**
     * Requires Items that are added to {@code this} to have tag {@code tag}.
     *
     * @param tag
     *            the tag to require
     *
     * @updates this
     *
     * @ensures tag is in this.restrictions
     */
    void restrict(String tag);

    /**
     * Removes all tag restrictions from {@code this}.
     *
     * @updates this
     *
     * @ensures this.restrictions = {}
     */
    void freeRestrictions();

    /**
     * Checks whether an Item passes the tag restrictions to be added to
     * {@code this}.
     *
     * @param item
     *            the Item to check against the restrictions
     * @return true if {@code item} passes intake restrictions, and false
     *         otherwise
     * @requires item is not null
     *
     * @ensures isAllowed = true iff for all tags t in this.restrictions, t is
     *          in item.tags
     */
    boolean isAllowed(Inventory.Item item);

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
     * @requires 0 <= pos < |this|
     *
     * @ensures nextIndexOf >= 0 iff an Item called name is in {@code this} at
     *          slot pos or higher
     */
    int nextIndexOf(String name, int pos);

    /**
     * Returns the total number of slots in {@code this}.
     *
     * @return the number of slots in this Inventory
     *
     * @ensures size = |this|
     */
    int size();
}
