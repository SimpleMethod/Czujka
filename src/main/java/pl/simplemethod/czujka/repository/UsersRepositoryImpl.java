package pl.simplemethod.czujka.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.simplemethod.czujka.model.Users;

import java.util.List;
import java.util.Optional;

public class UsersRepositoryImpl implements UsersRepository {

    @Override
    public List<Users> findAll() {
        return null;
    }

    @Override
    public List<Users> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<Users> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public <S extends Users> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Users> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Users> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Users getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Users> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Users> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public Page<Users> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Users> S save(S s) {
        return null;
    }

    @Override
    public Optional<Users> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Users users) {

    }

    @Override
    public void deleteAll(Iterable<? extends Users> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Users> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Users> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Users> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Users> boolean exists(Example<S> example) {
        return false;
    }
}
