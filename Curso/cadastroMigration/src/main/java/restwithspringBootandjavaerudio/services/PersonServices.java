package restwithspringBootandjavaerudio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restwithspringBootandjavaerudio.data.vo.v1.PersonVO;
import restwithspringBootandjavaerudio.exceptions.ResourceNotFoundException;
import restwithspringBootandjavaerudio.mapper.DozerMapper;
import restwithspringBootandjavaerudio.model.Person;
import restwithspringBootandjavaerudio.repositories.PersonRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    public List<PersonVO> findAll() {
        logger.info("Find people!");

        return DozerMapper.parseListObject(repository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id) {
        logger.info("Find one person!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this ID!"));
        return DozerMapper.parseObject(entity, PersonVO.class);
    }

    public PersonVO create(PersonVO person) {
        logger.info("Creating one person!");
        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        return vo;
    }

    public PersonVO update(PersonVO person) {
        logger.info("Updating one person!");
        var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records founds for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this ID!"));
        repository.delete(entity);
    }
}
