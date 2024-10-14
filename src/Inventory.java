/**
 * {@code InventoryKernel} enhanced with secondary methods.
 *
 * @author David Stuckey
 */
public interface Inventory extends InventoryKernel {

    /**
     * Returns the Item at {@code slot} in {@code this}.
     *
     * @param slot
     *            the slot to get the Item from
     * @return the Item at {@code slot}
     *
     * @requires 0 <= slot < |this|
     *
     * @ensures getItem = this[slot]
     *
     * @aliases the Item in the slot
     */
    Item getItem(int slot);

    /**
     * Adds a copy of an Item in another Inventory to {@code this}.
     *
     * @param src
     *            the source Inventory
     * @param name
     *            the slot to get the Item from
     * @param destSlot
     *            where to place the copied Item in {@code this}
     * @updates this
     *
     * @requires <pre>
     * -an Item with name {@code name} is in {@code src}
     *- 0 <= destSlot < |this|
     *- Where 'item' is the Item to be copied:
     *  - this[destSlot] = empty OR this[destSlot].name = item.name
     *  - this.isAllowed(item)
     *</pre>
     * @ensures <pre>
     * - this[destSlot].name = name
     * Where 'item' is the Item to be copied:
     *  - this[destSlot].tags contains has a tag t iff t is in item.tags
     *</pre>
     */
    void copyItem(InventoryConcept src, String name, int destSlot);

    /**
     * Swaps the Items in 2 slots of {@code this}.
     *
     * @param slot1
     *            the first slot
     * @param slot2
     *            the second slot
     * @updates this
     *
     * @requires 0 <= slot1 < |this|, 0 <= slot2 < |this|, slot1 != slot2
     *
     * @ensures this[slot1] = #this[slot2], this[slot2] = #this[slot1]
     */
    void swapItems(int slot1, int slot2);

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
     * @updates this, src
     *
     * @requires 0 <= srcSlot < |src|, 0 <= destSlot < |this|, src is not null
     *
     * @ensures this[destSlot] = #src[srcSlot], src[srcSlot] = #this[destSlot]
     */
    void swapItems(InventoryConcept src, int srcSlot, int destSlot);

    /**
     * Transfers an Item from another Inventory to {@code this}.
     *
     * @param src
     *            the Inventory to transfer from
     *
     * @param srcSlot
     *            the slot in {@code src} to transfer from
     * @param destSlot
     *            the slot in {@code this} to place the Item at
     * @updates this, src
     *
     * @requires <pre>
     * 0 <= srcSlot < |src|, 0 <= destSlot < |this|, src is not null
     * this[destSlot] = empty OR this[destSlot].name = src[srcSlot].name
     * this.isAllowed(src[srcSlot])
     * </pre>
     *
     * @ensures this[destSlot] = #src[srcSlot], src[srcSlot]=Item.EMPTY
     */
    void transferItem(InventoryConcept src, int srcSlot, int destSlot);

    /**
     * Returns position of the first slot in {@code this} at which {@code item}
     * could be added to {@code this}, or -1 if no such slot exists. The method
     * will attempt to stack Items before placing in an empty slot.
     *
     * @param item
     *            the item to be placed
     * @param maxStack
     *            the maximum stack size desired, or 0 if you want unbounded
     *            stacking
     * @return the index of the first such slot, or -1 if there are none
     *
     * @requires item is not null
     *
     * @ensures <pre>
     *  -this.addItem(nextPlacement, item) is a valid method call (i.e.
     * it fulfills the requires clause) if nextPlacement >= 0
     *
     *-item.count + this[nextPlacement].count <= maxStack iff
     * nextPlacement >=0
     * </pre>
     */
    int nextPlacement(Item item, int maxStack);

    /**
     * Removes a single Item at slot {@code slot} in {@code this}.
     *
     * @param slot
     *            the slot at which to use the Item
     * @return the name of the used Item
     *
     * @updates this
     *
     * @requires 0 <= slot < |this|
     *
     * @ensures <pre>
     * - this[slot].count = max(#this[slot].count - 1, 0)
     *- if this[slot].count = 0, this[slot] = Item.EMPTY
     *</pre>
     */
    String useItem(int slot);

    /**
     * Checks whether an Item with name {@code name} is at {@code slot} in
     * {@code this}.
     *
     * @param slot
     *            the slot to check
     * @param name
     *            the name of the Item to check for
     * @return true if the Item at {@code slot} has the correct name, and false
     *         otherwise
     *
     * @requires 0< = slot < |this|
     *
     * @ensures isAt = (this[slot].name == name)
     */
    boolean isAt(int slot, String name);

    /** Representation of a single item in the Inventory. */
    interface Item {

        /** Constant for count tag. An item is guaranteed to have this tag. */
        String COUNT = "count";

        /** Constant for empty item name. */
        String EMPTY_NAME = "";

        /**
         * Returns whether this is an empty Item.
         *
         * @return whether {@code this} is empty.
         *
         * @ensures isEmpty = (this.name == Item.EMPTY_NAME)
         */
        boolean isEmpty();

        /**
         * Returns the name/identifier of this Item.
         *
         * @return the name of {@code this}.
         *
         * @ensures getName = this.name
         */
        String getName();

        /**
         * Returns whether this Item has the tag {@code tag}.
         *
         * @param tag
         *            the tag to check {@code this} for.
         *
         * @return true if the Item has the tag {@code tag} and false otherwise.
         *
         * @ensures hasTag = ( (tag, any) is in this.tags )
         */
        boolean hasTag(String tag);

        /**
         * If the tag is not already present, adds the {@code (tag,value)} pair
         * to this Item, otherwise updates the value associated with the tag.
         *
         * @param tag
         *            the tag to add / update
         * @param tagVal
         *            the tag value to add / update
         *
         * @updates this
         *
         * @ensures (tag, tagVal) is in this.tags
         */
        void putTag(String tag, int tagVal);

        /**
         * Removes {@code tag} from this Item.
         *
         * @param tag
         *            the tag to remove from {@code this}
         *
         * @requires <pre>
         * - (tag, any) is in this.tags.
         *- tag != Item.COUNT
         * </pre>
         * @ensures (tag, any) is not in this.tags
         */
        void removeTag(String tag);

        /**
         * Returns the integer tag value associated with {@code tag}.
         *
         * @param tag
         *            The tag to get the associated value of.
         * @return The integer value associated with {@code tag}.
         *
         * @requires (tag, x) is in this.tags, where x is an integer
         *
         * @ensures tagValue = x
         */
        int tagValue(String tag);
    }
}
