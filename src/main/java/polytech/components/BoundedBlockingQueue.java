package polytech.components;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BoundedBlockingQueue<E> implements BlockingQueue<E> {
    private final BlockingQueue<E> queue;
    private final Semaphore semaphore;

    public BoundedBlockingQueue(BlockingQueue<E> queue, int capacity) {
        this.queue = queue;
        this.semaphore = new Semaphore(capacity);
    }

    @Override
    public void put(E element) throws InterruptedException {
        semaphore.acquire();
        queue.put(element);
    }

    @Override
    public E take() throws InterruptedException {
        E element = queue.take();
        semaphore.release();
        return element;
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean add(E e) {
        return queue.add(e);
    }

    @Override
    public boolean offer(E e) {
        return queue.offer(e);
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return queue.offer(e, timeout, unit);
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    @Override
    public int remainingCapacity() {
        return queue.remainingCapacity();
    }

    @Override
    public boolean remove(Object o) {
        return queue.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return queue.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return queue.drainTo(c, maxElements);
    }

    @Override
    public E remove() {
        return queue.remove();
    }

    @Override
    public E poll() {
        return queue.poll();
    }

    @Override
    public E element() {
        return queue.element();
    }

    @Override
    public E peek() {
        return queue.peek();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return queue.toArray(a);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return queue.toArray(generator);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return queue.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return queue.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return queue.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return queue.retainAll(c);
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean equals(Object o) {
        return queue.equals(o);
    }

    @Override
    public int hashCode() {
        return queue.hashCode();
    }

    @Override
    public Spliterator<E> spliterator() {
        return queue.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return queue.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return queue.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        queue.forEach(action);
    }
}
