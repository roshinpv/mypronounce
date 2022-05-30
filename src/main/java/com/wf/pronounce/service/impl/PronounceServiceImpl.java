package com.wf.pronounce.service.impl;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.wf.pronounce.domain.Pronounce;
import com.wf.pronounce.repository.PronounceRepository;
import com.wf.pronounce.service.PronounceService;
import com.wf.pronounce.service.dto.PronounceDTO;
import com.wf.pronounce.service.mapper.PronounceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Pronounce}.
 */
@Service
@Transactional
public class PronounceServiceImpl implements PronounceService {

    private final Logger log = LoggerFactory.getLogger(PronounceServiceImpl.class);

    private final PronounceRepository pronounceRepository;

    private final PronounceMapper pronounceMapper;

    public PronounceServiceImpl(PronounceRepository pronounceRepository, PronounceMapper pronounceMapper) {
        this.pronounceRepository = pronounceRepository;
        this.pronounceMapper = pronounceMapper;
    }

    @Override
    public Mono<PronounceDTO> save(PronounceDTO pronounceDTO) {
        log.debug("Request to save Pronounce : {}", pronounceDTO);
        pronounceDTO = createPronounce(pronounceDTO);
        return pronounceRepository.save(pronounceMapper.toEntity(pronounceDTO)).map(pronounceMapper::toDto);
    }

    @Override
    public Mono<PronounceDTO> update(PronounceDTO pronounceDTO) {
        log.debug("Request to save Pronounce : {}", pronounceDTO);
        pronounceDTO = createPronounce(pronounceDTO);
        return pronounceRepository.save(pronounceMapper.toEntity(pronounceDTO)).map(pronounceMapper::toDto);
    }

    @Override
    public Mono<PronounceDTO> partialUpdate(PronounceDTO pronounceDTO) {
        log.debug("Request to partially update Pronounce : {}", pronounceDTO);
        return pronounceRepository
            .findById(pronounceDTO.getId())
            .map(existingPronounce -> {
                pronounceMapper.partialUpdate(existingPronounce, createPronounce(pronounceDTO));

                return existingPronounce;
            })
            .flatMap(pronounceRepository::save)
            .map(pronounceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PronounceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pronounces");
        return pronounceRepository.findAllBy(pageable).map(pronounceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return pronounceRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PronounceDTO> findOne(Long id) {
        log.debug("Request to get Pronounce : {}", id);
        return pronounceRepository.findById(id).map(pronounceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PronounceDTO> findOne(String login) {
        log.debug("Request to get Pronounce : {}", login);
        return pronounceRepository.findByLogin(login).map(pronounceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Pronounce : {}", id);
        return pronounceRepository.deleteById(id);
    }


    private PronounceDTO createPronounce(PronounceDTO pronounceDTO) {

        String text = pronounceDTO.getPreferredName();
        PronounceDTO tempPronounceDTO = null ;
        if (pronounceDTO.getPronunciation() == null) {
            if (text == null || text.isBlank()) {
                text = pronounceDTO.getFirstName() + " " + pronounceDTO.getLastName();
            }
        }

        pronounceDTO.setPronunciationContentType("audio/mpeg");
        pronounceDTO.setPronunciation(generateAudio(text, "IN"));

        return pronounceDTO;
    }

    @Override
    public byte[] generateAudio(String name, String country) {

        ByteString audioContents = null;

        if (name != null) {
            try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
                SynthesisInput input = SynthesisInput.newBuilder().setText(name).build();
                VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                        .setLanguageCode("en-IN")
                        .setName("en-IN-Standard-C")
                        .build();
                AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
                SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
                audioContents = response.getAudioContent();

            }
            catch (Exception e) {
                System.out.println("here 1" + e.getMessage());
            }

        }
        return audioContents.toByteArray();
    }


}
