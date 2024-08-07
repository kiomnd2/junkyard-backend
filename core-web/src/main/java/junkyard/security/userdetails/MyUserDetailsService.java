package junkyard.security.userdetails;

import junkyard.member.domain.MemberReader;
import junkyard.member.domain.MemberUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberReader memberReader;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long authId = Long.parseLong(username);
        Optional<MemberUser> memberUser = memberReader.readByAuthId(authId);
        MemberUser user = memberUser.orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다 "));
        return new MyUserDetails(user);
    }
}
